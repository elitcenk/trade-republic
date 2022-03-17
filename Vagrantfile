# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANT_API_VERSION = "2"

Vagrant.configure(VAGRANT_API_VERSION) do |config|

  config.vm.box = "ubuntu/xenial64"

  config.vm.define :"trade-republic-vagrant" do |t|
  end

  config.ssh.forward_agent = true
  config.ssh.forward_x11 = true

  config.vm.provider "virtualbox" do |vb|

	vb.gui = false

	# Customize the virtual box name
	vb.name = "trade-republic--vagrant-machine"

	vb.customize ["modifyvm", :id, "--natdnshostresolver1", "on"]
    vb.customize ["modifyvm", :id, "--natdnsproxy1", "on"]
	vb.customize ['modifyvm', :id, '--cableconnected1', 'on']

	# Customize the amount of memory on the VM:
	vb.memory = "4096"
    # vb.memory = "16384"
    vb.cpus = 4
    # vb.cpus = 8
  end

  config.vm.synced_folder ".", "/republic", type: "rsync"

  # Create a private network, which allows host-only access to the machine using a specific IP.
  config.vm.network "private_network", ip: "192.168.33.10"

  # If errors occur, try running "vagrant provision" manually after "vagrant up"
  config.vm.provision :docker
  config.vm.provision :docker_compose

  # start partner service
  config.vm.network "forwarded_port", guest: 8032, host: 8032, disabled: false
  config.vm.provision :docker_compose, rebuild: true, yml: "/republic/docker/partner-service/docker-compose.yml", run: "always"

  # start mongo
  config.vm.network "forwarded_port", guest: 8081, host: 8081, disabled: false
  config.vm.network "forwarded_port", guest: 27017, host: 27017, disabled: false
  config.vm.provision :docker_compose, rebuild: true, yml: "/republic/docker/mongodb/docker-compose.yml", run: "always"

  config.vm.provision "run", type: "shell", privileged: false, run: "always",
	inline: <<-SHELL
	  echo "=============================================="
	  echo "dependencies are ready !"
	  docker ps --format "{{.ID}}\t{{.Names}}\t{{.Ports}}"
	  echo "=============================================="
  SHELL

end
